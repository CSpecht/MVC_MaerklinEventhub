package specht.General;

public class ImmutableObjectException extends UnsupportedOperationException
{
    private static final long serialVersionUID = 3988487445782905032L;
    public final Object immutable;

    public ImmutableObjectException(Object o)
    {
        super("This Object is immutable. Please make a copy bevore you try to change it");
        this.immutable = o;
    }

    public ImmutableObjectException(String message, Throwable cause, Object o)
    {
        super(message, cause);
        this.immutable = o;
    }

    public ImmutableObjectException(String message, Object o)
    {
        super(message);
        this.immutable = o;
    }

    public ImmutableObjectException(Throwable cause, Object o)
    {
        super(cause);
        this.immutable = o;
    }
}

