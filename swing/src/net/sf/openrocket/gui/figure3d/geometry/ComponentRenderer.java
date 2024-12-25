package net.sf.openrocket.gui.figure3d.geometry;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;

import net.sf.openrocket.gui.figure3d.RocketRenderer;
import net.sf.openrocket.rocketcomponent.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.openrocket.gui.figure3d.geometry.Geometry.Surface;
import net.sf.openrocket.motor.Motor;
import net.sf.openrocket.rocketcomponent.Transition.Shape;
import net.sf.openrocket.util.Coordinate;
import net.sf.openrocket.util.Transformation;
import retrofit2.http.Body;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/*
 * @author Bill Kuker <bkuker@billkuker.com>
 * @author Daniel Williams <equipoise@gmail.com>
 */
public class ComponentRenderer {
    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(ComponentRenderer.class);

    private int LOD = 80;        // Level of detail for rendering

    GLU glu;
    GLUquadric q;
    FinRenderer fr = new FinRenderer();

    public ComponentRenderer() {

    }

    public void init(GLAutoDrawable drawable) {
        glu = new GLU();
        q = glu.gluNewQuadric();
        glu.gluQuadricTexture(q, true);
    }


    public void updateFigure(GLAutoDrawable drawable) {

    }

    public Geometry getComponentGeometry(final RocketComponent comp) {
        return getComponentGeometry(comp, Transformation.IDENTITY);
    }

    public Geometry getComponentGeometry(final RocketComponent comp, final Transformation transform) {
        return new Geometry(comp, transform) {
            @Override
            public void render(GL2 gl, final Surface which) {
                gl.glPushMatrix();

                gl.glMultMatrixd(transform.getGLMatrix());
                if (which == Surface.ALL) {
                    renderInstance(gl, comp, Surface.INSIDE);
                    renderInstance(gl, comp, Surface.EDGES);
                    renderInstance(gl, comp, Surface.OUTSIDE);
                } else {
                    renderInstance(gl, comp, which);
                }
                gl.glPopMatrix();
            }
        };
    }

    public Geometry getMotorGeometry(final Motor motor) {
        return new Geometry(motor, Transformation.IDENTITY) {
            @Override
            public void render(GL2 gl, final Surface which) {
                renderMotor(gl, motor);
            }
        };
    }

    protected void renderInstance(GL2 gl, RocketComponent c, Surface which) {
        if (glu == null)
            throw new IllegalStateException(this + " Not Initialized");

        glu.gluQuadricNormals(q, GLU.GLU_SMOOTH);

        if (c instanceof BodyTube) {
            renderTube(gl, (BodyTube) c, which);
        } else if (c instanceof InnerTube) {
            renderTube(gl, (InnerTube) c, which);
        } else if (c instanceof LaunchLug) {
            renderLug(gl, (LaunchLug) c, which);
        } else if (c instanceof RailButton) {
            renderRailButton(gl, (RailButton) c, which);
        } else if (c instanceof RingComponent) {
            if (which == Surface.OUTSIDE)
                renderRing(gl, (RingComponent) c);
        } else if (c instanceof Transition) {
            renderTransition(gl, (Transition) c, which);
        } else if (c instanceof MassObject) {
            if (which == Surface.OUTSIDE)
                renderMassObject(gl, (MassObject) c);
        } else if (c instanceof FinSet) {
            FinSet fins = (FinSet) c;
            fr.renderFinSet(gl, fins, which);
        } else if (c instanceof TubeFinSet) {
            renderTubeFins(gl, (TubeFinSet) c, which);
        } else if (c instanceof AxialStage) {
        } else if (c instanceof ParallelStage) {
        } else if (c instanceof PodSet) {
        } else {
            renderOther(gl, c);
        }


    }


    private void renderOther(GL2 gl, RocketComponent c) {
        gl.glBegin(GL.GL_LINES);
        for (Coordinate cc : c.getComponentBounds()) {
            for (Coordinate ccc : c.getComponentBounds()) {
                gl.glVertex3d(cc.x, cc.y, cc.z);
                gl.glVertex3d(ccc.x, ccc.y, ccc.z);
            }
        }

        gl.glEnd();
    }

    private void renderTransition(GL2 gl, Transition t, Surface which) {
        if (which == Surface.OUTSIDE || which == Surface.INSIDE) {
            gl.glPushMatrix();
            gl.glRotated(90, 0, 1.0, 0);
            if (which == Surface.INSIDE) {
                gl.glFrontFace(GL.GL_CCW);
            }
            TransitionRenderer.drawTransition(gl, t, LOD, t.getShapeType() == Shape.CONICAL ? 4 : LOD / 2, which == Surface.INSIDE ? -t.getThickness() : 0,which);
            if (which == Surface.INSIDE) {
                gl.glFrontFace(GL.GL_CW);
            }
            gl.glPopMatrix();
        }

        if (which == Surface.EDGES || which == Surface.INSIDE) {
            //Render aft edge
            gl.glPushMatrix();
            gl.glTranslated(t.getLength(), 0, 0);
            if (which == Surface.EDGES) {
                gl.glRotated(90, 0, 1.0, 0);
                glu.gluDisk(q, Math.max(0, t.getAftRadius() - t.getThickness()), t.getAftRadius(), LOD, 2);
            } else {
                gl.glRotated(270, 0, 1.0, 0);
                glu.gluDisk(q, Math.max(0, t.getAftRadius() - t.getThickness()), t.getAftRadius(), LOD, 2);
            }
            gl.glPopMatrix();

            // Render AFT shoulder
            if (t.getAftShoulderLength() > 0) {
                gl.glPushMatrix();
                gl.glTranslated(t.getLength(), 0, 0);
                double iR = (t.isFilled() || t.isAftShoulderCapped()) ? 0 : t.getAftShoulderRadius() - t.getAftShoulderThickness();
                if (which == Surface.EDGES) {
                    renderTube(gl, Surface.OUTSIDE, t.getAftShoulderRadius(), iR, t.getAftShoulderLength(),(RocketComponent) t);
                    renderTube(gl, Surface.EDGES, t.getAftShoulderRadius(), iR, t.getAftShoulderLength(),(RocketComponent) t);
                    gl.glPushMatrix();
                    gl.glRotated(90, 0, 1.0, 0);
                    glu.gluDisk(q, t.getAftShoulderRadius(), t.getAftRadius(), LOD, 2);
                    gl.glPopMatrix();

                } else {
                    renderTube(gl, Surface.INSIDE, t.getAftShoulderRadius(), iR, t.getAftShoulderLength(),(RocketComponent) t);
                    gl.glPushMatrix();
                    gl.glRotated(270, 0, 1.0, 0);
                    glu.gluDisk(q, t.getAftShoulderRadius(), t.getAftRadius(), LOD, 2);
                    gl.glPopMatrix();
                }
                gl.glPopMatrix();
            }

            //Render Fore edge
            gl.glPushMatrix();
            gl.glRotated(180, 0, 1.0, 0);
            if (which == Surface.EDGES) {
                gl.glRotated(90, 0, 1.0, 0);
                glu.gluDisk(q, Math.max(0, t.getForeRadius() - t.getThickness()), t.getForeRadius(), LOD, 2);
            } else {
                gl.glRotated(270, 0, 1.0, 0);
                glu.gluDisk(q, Math.max(0, t.getForeRadius() - t.getThickness()), t.getForeRadius(), LOD, 2);
            }
            gl.glPopMatrix();

            // Render Fore shoulder
            if (t.getForeShoulderLength() > 0) {
                gl.glPushMatrix();
                gl.glRotated(180, 0, 1.0, 0);
                //gl.glTranslated(t.getLengthAerodynamic(), 0, 0);
                double iR = (t.isFilled() || t.isForeShoulderCapped()) ? 0 : t.getForeShoulderRadius() - t.getForeShoulderThickness();
                if (which == Surface.EDGES) {
                    renderTube(gl, Surface.OUTSIDE, t.getForeShoulderRadius(), iR, t.getForeShoulderLength(),t);
                    renderTube(gl, Surface.EDGES, t.getForeShoulderRadius(), iR, t.getForeShoulderLength(),t);
                    gl.glPushMatrix();
                    gl.glRotated(90, 0, 1.0, 0);
                    glu.gluDisk(q, t.getForeShoulderRadius(), t.getForeRadius(), LOD, 2);
                    gl.glPopMatrix();

                } else {
                    renderTube(gl, Surface.INSIDE, t.getForeShoulderRadius(), iR, t.getForeShoulderLength(),t);
                    gl.glPushMatrix();
                    gl.glRotated(270, 0, 1.0, 0);
                    glu.gluDisk(q, t.getForeShoulderRadius(), t.getForeRadius(), LOD, 2);
                    gl.glPopMatrix();
                }
                gl.glPopMatrix();
            }

        }

    }

    private void renderTube(final GL2 gl, final Surface which, final double oR, final double iR, final double len, RocketComponent component) {
        gl.glPushMatrix();
        //outside
        gl.glRotated(90, 0, 1.0, 0);
        if (which == Surface.OUTSIDE) {
            glu.gluCylinder(q, oR, oR, len, LOD, 1);
            // 提取外表坐标
            List<Coordinate> outerCoordinates = extractOuterCoordinates(gl, oR, len, LOD,90);
            if (component instanceof BodyTube){
                saveOuterCoordinates(outerCoordinates);
            }
        }
        //edges
        gl.glRotated(180, 0, 1.0, 0);
        if (which == Surface.EDGES)
            glu.gluDisk(q, iR, oR, LOD, 2);

        gl.glRotated(180, 0, 1.0, 0);
        gl.glTranslated(0, 0, len);
        if (which == Surface.EDGES)
            glu.gluDisk(q, iR, oR, LOD, 2);

        //inside
        if (which == Surface.INSIDE) {
            glu.gluQuadricOrientation(q, GLU.GLU_INSIDE);
            glu.gluCylinder(q, iR, iR, -len, LOD, 1);
            glu.gluQuadricOrientation(q, GLU.GLU_OUTSIDE);
        }
        gl.glPopMatrix();
    }
    private List<Coordinate> extractOuterCoordinates(final GL2 gl, final double oR, final double len, final int LOD, final int zDivisions) {
        List<Coordinate> coordinates = new ArrayList<>();

        // 圆周上的分段数，LOD 控制圆周密度
        int numSegments = LOD;
        double angleStep = 2 * Math.PI / numSegments;

        // Z 方向的分段步长，zDivisions 控制高度方向的密度
        double zStep = len / zDivisions;

        // 从 Z=0 开始逐层采样
        for (int j = 0; j <= zDivisions; j++) {
            double z = j * zStep; // 当前高度

            // 在当前高度的圆周上采样点
            for (int i = 0; i < numSegments; i++) {
                double angle = i * angleStep; // 当前角度

                // 计算圆周点的 X 和 Y 坐标
                double x = oR * Math.cos(angle);
                double y = oR * Math.sin(angle);

                // 添加点到列表
                coordinates.add(new Coordinate(x, y, z));
            }
        }

        return coordinates;
    }




    // 插值方法
    private void interpolateCoordinates(List<Coordinate> coordinates, double oR, double len, int numSegments, int zDivisions) {
        double angleStep = Math.PI / (2 * numSegments); // 半角步长
        double zStep = len / (2 * zDivisions); // 半高度步长

        // 对原点之间进行插值
        List<Coordinate> interpolated = new ArrayList<>();
        for (Coordinate coord : coordinates) {
            interpolated.add(new Coordinate(coord.x, coord.y, coord.z));
            interpolated.add(new Coordinate(
                    coord.x + oR * Math.cos(angleStep),
                    coord.y + oR * Math.sin(angleStep),
                    coord.z + zStep
            ));
        }
        coordinates.addAll(interpolated);
    }



    public void saveOuterCoordinates(List<Coordinate> coordinates) {
        System.out.println("Captured outer surface coordinates:");

        // 定义文件路径
        String filePath = "E://bodytube.txt";

        // 使用 FileWriter 的追加模式
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) { // true 表示追加模式
            // 遍历坐标列表，将每个坐标写入文件
            for (Coordinate coordinate : coordinates) {
                String formattedCoordinate = String.format("(%s, %s, %s),",
                        coordinate.x, coordinate.y, coordinate.z);
                writer.write(formattedCoordinate);
                writer.newLine(); // 换行
            }


            System.out.println("Coordinates saved to " + filePath);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }



    private void renderTube(GL2 gl, BodyTube t, Surface which) {
        renderTube(gl, which, t.getOuterRadius(), t.getInnerRadius(), t.getLength(),t);
    }

    private void renderTube(GL2 gl, InnerTube t, Surface which) {
        renderTube(gl, which, t.getOuterRadius(), t.getInnerRadius(), t.getLength(),t);
    }

    private void renderRing(GL2 gl, RingComponent r) {

        gl.glRotated(90, 0, 1.0, 0);
        glu.gluCylinder(q, r.getOuterRadius(), r.getOuterRadius(),
                r.getLength(), LOD, 1);

        gl.glRotated(180, 0, 1.0, 0);
        glu.gluDisk(q, r.getInnerRadius(), r.getOuterRadius(), LOD, 2);

        gl.glRotated(180, 0, 1.0, 0);
        gl.glTranslated(0, 0, r.getLength());
        glu.gluDisk(q, r.getInnerRadius(), r.getOuterRadius(), LOD, 2);

        glu.gluQuadricOrientation(q, GLU.GLU_INSIDE);
        glu.gluCylinder(q, r.getInnerRadius(), r.getInnerRadius(),
                -r.getLength(), LOD, 1);
        glu.gluQuadricOrientation(q, GLU.GLU_OUTSIDE);

    }

    private void renderLug(GL2 gl, LaunchLug t, Surface which) {
        renderTube(gl, which, t.getOuterRadius(), t.getInnerRadius(), t.getLength(),t);
    }

    private void renderRailButton(GL2 gl, RailButton r, Surface which) {
        if (which == Surface.OUTSIDE) {
            //renderOther(gl, r);
            final double or = r.getOuterDiameter() / 2.0;
            final double ir = r.getInnerDiameter() / 2.0;
            gl.glRotated(r.getAngleOffset() * 180 / Math.PI - 90, 1, 0, 0);

            // Base Cylinder
            if (r.getBaseHeight() > 0) {
                glu.gluCylinder(q, or, or, r.getBaseHeight(), LOD, 1);
                glu.gluQuadricOrientation(q, GLU.GLU_INSIDE);
                glu.gluDisk(q, 0, or, LOD, 2);
                glu.gluQuadricOrientation(q, GLU.GLU_OUTSIDE);
                gl.glTranslated(0, 0, r.getBaseHeight());
                glu.gluDisk(q, 0, or, LOD, 2);
            } else {    // Draw a closing cap if there is no base
                glu.gluQuadricOrientation(q, GLU.GLU_INSIDE);
                glu.gluDisk(q, 0, ir, LOD, 2);
                glu.gluQuadricOrientation(q, GLU.GLU_OUTSIDE);
                gl.glTranslated(0, 0, r.getBaseHeight());
            }

            // Inner Cylinder
            glu.gluCylinder(q, ir, ir, r.getInnerHeight(), LOD, 1);

            // Flange Cylinder
            gl.glTranslated(0, 0, r.getInnerHeight());
            if (r.getFlangeHeight() > 0) {
                glu.gluCylinder(q, or, or, r.getFlangeHeight(), LOD, 1);
                glu.gluQuadricOrientation(q, GLU.GLU_INSIDE);
                glu.gluDisk(q, 0, or, LOD, 2);
                glu.gluQuadricOrientation(q, GLU.GLU_OUTSIDE);
                gl.glTranslated(0, 0, r.getFlangeHeight());
                glu.gluDisk(q, 0, or, LOD, 2);
            } else if (r.getScrewHeight() == 0) {    // Draw a closing cap if there is no flange
                glu.gluDisk(q, 0, ir, LOD, 2);
            }

            // Screw
            if (r.getScrewHeight() > 0) {
                // Half dome
                gl.glClipPlane(GL2.GL_CLIP_PLANE0, new double[]{0, 0, 1, 0}, 0);
                gl.glEnable(GL2.GL_CLIP_PLANE0);
                gl.glScaled(1, 1, r.getScrewHeight() / (r.getOuterDiameter() / 2));
                glu.gluSphere(q, r.getOuterDiameter() / 2.0, LOD, LOD);
                gl.glDisable(GL2.GL_CLIP_PLANE0);

                // Closing disk
                glu.gluQuadricOrientation(q, GLU.GLU_INSIDE);
                glu.gluDisk(q, ir, or, LOD, 2);
            }

        }
    }

    private void renderTubeFins(GL2 gl, TubeFinSet fs, Surface which) {
        gl.glPushMatrix();
        gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
        gl.glTranslated(0, fs.getOuterRadius(), 0);
        renderTube(gl, which, fs.getOuterRadius(), fs.getInnerRadius(), fs.getLength(),fs);
        gl.glPopMatrix();
    }

    private void renderMassObject(GL2 gl, MassObject o) {
        gl.glRotated(90, 0, 1.0, 0);

        MassObjectRenderer.drawMassObject(gl, o, LOD / 2, LOD / 2);
    }

    private void renderMotor(final GL2 gl, Motor motor) {
        double l = motor.getLength();
        double r = motor.getDiameter() / 2;

        gl.glPushMatrix();

        gl.glRotated(90, 0, 1.0, 0);

        gl.glMatrixMode(GL.GL_TEXTURE);
        gl.glPushMatrix();
        gl.glTranslated(0, .125, 0);
        gl.glScaled(1, .75, 0);

        glu.gluCylinder(q, r, r, l, LOD, 1);

        gl.glPopMatrix();
        gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);

        {
            final double da = (2.0f * Math.PI) / LOD;
            final double dt = 1.0 / LOD;
            gl.glBegin(GL.GL_TRIANGLE_STRIP);
            gl.glNormal3d(0, 0, 1);
            for (int i = 0; i < LOD + 1; i++) {
                gl.glTexCoord2d(i * dt, .125);
                gl.glVertex3d(r * Math.cos(da * i), r * Math.sin(da * i), 0);
                gl.glTexCoord2d(i * dt, 0);
                gl.glVertex3d(0, 0, 0);

            }
            gl.glEnd();
        }

        gl.glTranslated(0, 0, l);
        gl.glRotated(180, 0, 1.0, 0);

        {
            final double da = (2.0f * Math.PI) / LOD;
            final double dt = 1.0 / LOD;
            gl.glBegin(GL.GL_TRIANGLE_STRIP);
            gl.glNormal3d(0, 0, -1);
            for (int i = 0; i < LOD + 1; i++) {
                gl.glTexCoord2d(i * dt, .875);
                gl.glVertex3d(r * Math.cos(da * i), r * Math.sin(da * i), 0);
                gl.glTexCoord2d(i * dt, .9);
                gl.glVertex3d(.8 * r * Math.cos(da * i), .8 * r * Math.sin(da * i), 0);
            }
            gl.glEnd();
            gl.glBegin(GL.GL_TRIANGLE_STRIP);

            for (int i = 0; i < LOD + 1; i++) {
                gl.glNormal3d(-Math.cos(da * i), -Math.sin(da * i), -1);
                gl.glTexCoord2d(i * dt, .9);
                gl.glVertex3d(.8 * r * Math.cos(da * i), .8 * r * Math.sin(da * i), 0);
                gl.glTexCoord2d(i * dt, 1);
                gl.glVertex3d(0, 0, l * .05);
            }
            gl.glEnd();
        }
        gl.glPopMatrix();
    }
}
