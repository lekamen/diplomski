package hr.lenak.diplomski.core.processing.textrank;

import java.util.ArrayList;
import java.util.List;

public class Brid {

	private final Vrh lijevi;
	private final Vrh desni;
	
	private List<Brid> svePojaveBrida = new ArrayList<Brid>();

	public Brid(Vrh lijevi, Vrh desni) {
		this.lijevi = lijevi;
		this.desni = desni;
		
		svePojaveBrida.add(this);
	}

	public Vrh getLijevi() {
		return lijevi;
	}

	public Vrh getDesni() {
		return desni;
	}
	
	public List<Brid> getSvePojaveBrida() {
		return svePojaveBrida;
	}
	
	public boolean dodajPojavuBrida(Brid brid) {
		return svePojaveBrida.add(brid);
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
	
	public int getUkupanBroj() {
		return svePojaveBrida.size();
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
		sb.append("Brid[lijevi=").append(lijevi).append(",desni").append(desni).append(",pojave=").append(svePojaveBrida.size()).append("]");
		return sb.toString();
	}
}
