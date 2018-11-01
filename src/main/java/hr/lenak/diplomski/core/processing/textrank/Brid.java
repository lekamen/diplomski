package hr.lenak.diplomski.core.processing.textrank;

public class Brid {

	private final Vrh lijevi;
	private final Vrh desni;

	public Brid(Vrh lijevi, Vrh desni) {
		this.lijevi = lijevi;
		this.desni = desni;
	}

	public Vrh getLijevi() {
		return lijevi;
	}

	public Vrh getDesni() {
		return desni;
	}

	public Vrh vratiSusjedniVrh(Vrh vrh) {
		if (!isVrhNaBridu(vrh)) {
			return null;
		}
		
		if (lijevi.equals(vrh)) {
			return desni;
		}
		//onda je desni ovaj vrh
		return lijevi;
	}

	public boolean isVrhNaBridu(Vrh vrh) {
		return lijevi.equals(vrh) || desni.equals(vrh);
	}

	@Override
	public int hashCode() {
		return lijevi.hashCode() ^ desni.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Brid))
			return false;
		Brid other = (Brid) o;
		return (this.lijevi.equals(other.lijevi) && this.desni.equals(other.desni))
			|| (this.lijevi.equals(other.desni) && this.desni.equals(other.lijevi));
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Brid[lijevi=").append(lijevi).append(",desni").append(desni).append("]");
		return sb.toString();
	}
}
