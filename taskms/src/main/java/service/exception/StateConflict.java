package service.exception;

import java.io.Serializable;

/**
 * Created by rohan on 2/23/17.
 */
public class StateConflict extends RuntimeException implements Serializable
{
    private static final long serialVersionUID = 1L;
    public StateConflict() {
        super();
    }
    public StateConflict(String msg)   {
        super(msg);
    }
    public StateConflict(String msg, Exception e)  {
        super(msg, e);
    }
}