package net.sf.openrocket.utils.educoder;

import java.util.Collection;

public class WholeCgRequest extends Request{
    private MyComponent myComponent;

    public MyComponent getMyComponent() {
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
