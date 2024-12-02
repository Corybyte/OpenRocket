package net.sf.openrocket.utils.educoder;

import net.sf.openrocket.utils.educoder.MyComponent;
import net.sf.openrocket.utils.educoder.Request;

public class WholeCgRequest extends Request {
    private net.sf.openrocket.utils.educoder.MyComponent myComponent;

    public net.sf.openrocket.utils.educoder.MyComponent getMyComponent() {
        return myComponent;
    }

    public void setMyComponent(MyComponent myComponent) {
        this.myComponent = myComponent;
    }

    @Override
    public String toString() {
        return "WholeCgRequest{" +
                "myComponent=" + myComponent +
                ", answer=" + answer +
                '}';
    }
}
