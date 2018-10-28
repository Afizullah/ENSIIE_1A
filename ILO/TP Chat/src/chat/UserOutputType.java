package chat;

import models.Message;

/**
 * Enumeration of client's output stream message capabilities to receive
 * messages from server
 */
public enum UserOutputType
{
	/**
	 * Client expects text messages only
	 */
	TEXT,
	/**
	 * Client expects {@link Message} objects
	 */
	OBJECT;

	/**
	 * Type string representation
	 */
	@Override
	public String toString()
	{
		switch (this)
		{
			case TEXT:
				return new String("Text output type");
			case OBJECT:
				return new String("Object output type");
		}
		throw new AssertionError("UserOutputType: unknown type: " + this);
	}

	/**
	 * Type conversion to integer
	 * @return The index corresponding to the type of output
	 * <ul>
	 * 	<li>TEXT = 1</li>
	 * 	<li>OBJECT = 2</li>
	 * </ul>
	 */
	public int toInteger()
	{
		return ordinal() + 1;
	}

	/**
	 * Factory method of a {@link UserOutputType} from integer index
	 * @param value the index of type to generate
	 * @return returns {@link #TEXT} if index <= 1 and {@link #OBJECT}
	 * if index is >= 2
	 */
	public static UserOutputType fromInteger(int value)
	{
		int controledValue;
		if (value < 1)
		{
			controledValue = 1;
		}
		else if (value > 2)
		{
			controledValue = 2;
		}
		else
		{
			controledValue = value;
		}
		switch (controledValue)
		{
			default:
			case 1:
				return TEXT;
			case 2:
				return OBJECT;
		}
	}
}
