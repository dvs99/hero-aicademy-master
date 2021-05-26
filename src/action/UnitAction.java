package action;

import model.Position;

import java.util.Objects;

public class UnitAction extends Action {

	public Position from;
	public Position to;
	public UnitActionType type;

	public static void main(String[] args) {
		UnitAction ua1 = new UnitAction(new Position(1,2), new Position(1,4), UnitActionType.ATTACK);
		UnitAction ua2 = new UnitAction(new Position(1,2), new Position(1,4), UnitActionType.ATTACK);
		System.out.println(ua1.hashCode());
		System.out.println(ua2.equals(ua1));
	}

	public UnitAction(Position from, Position to, UnitActionType type) {
		super();
		this.from = from;
		this.to = to;
		this.type = type;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UnitAction other = (UnitAction) obj;
		if (from == null) {
			if (other.from != null)
				return false;
		} else if (!from.equals(other.from))
			return false;
		if (to == null) {
			if (other.to != null)
				return false;
		} else if (!to.equals(other.to))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		return from.hashCode()*1000 + to.hashCode()*10 + type.ordinal();
	}

	@Override
	public String toString() {
		return "UnitAction [type= " + type + ", from=" + from + ", to=" + to + "]";
	}

}
