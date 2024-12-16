package net.sf.openrocket.utils.educoder;

import net.sf.openrocket.util.Coordinate;
import retrofit2.http.PUT;

import java.util.ArrayList;

public class DataRequest3 {
    public ArrayList componentInstance;
    public ArrayList cnaLists;
    public ArrayList cpLists;
    public ArrayList flags;
    public Double aoa;
    public Double refLength;

    public Double randomDoubles;

    public Double PitchDampingMoment;
    public Double YawDampingMoment;

    public Coordinate cm;
    public ArrayList tubeFinsetList;

    public ArrayList cRollDamps;
    public ArrayList cRollForces;


    public DataRequest3(ArrayList componentInstance, ArrayList cnaLists, ArrayList cpLists, ArrayList flags, Double aoa, Double refLength, Double randomDoubles, Double pitchDampingMoment, Double yawDampingMoment, Coordinate cm, ArrayList tubeFinsetList, ArrayList cRollDamps, ArrayList cRollForces) {
        this.componentInstance = componentInstance;
        this.cnaLists = cnaLists;
        this.cpLists = cpLists;
        this.flags = flags;
        this.aoa = aoa;
        this.refLength = refLength;
        this.randomDoubles = randomDoubles;
        PitchDampingMoment = pitchDampingMoment;
        YawDampingMoment = yawDampingMoment;
        this.cm = cm;
        this.tubeFinsetList = tubeFinsetList;
        this.cRollDamps = cRollDamps;
        this.cRollForces = cRollForces;
    }


}
