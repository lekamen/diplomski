package hr.lenak.diplomski.web.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import hr.lenak.diplomski.core.model.KljucneRijeci;
import hr.lenak.diplomski.core.model.TekstoviSluzbeni;
import hr.lenak.diplomski.core.processing.VrstaAlgoritmaEnum;
import hr.lenak.diplomski.web.views.PretragaPoKljucnimRijecimaView.KriterijPretrage;

public class RezultatiPretrage {

	private VrstaAlgoritmaEnum vrstaAlgoritma;
	private TekstoviSluzbeni tekstSluzbeni;
	
	public RezultatiPretrage(VrstaAlgoritmaEnum vrstaAlgoritma, TekstoviSluzbeni tekstSluzbeni) {
		this.vrstaAlgoritma = vrstaAlgoritma;
		this.tekstSluzbeni = tekstSluzbeni;
	}
	public VrstaAlgoritmaEnum getVrstaAlgoritma() {
		return vrstaAlgoritma;
	}
	public void setVrstaAlgoritma(VrstaAlgoritmaEnum vrstaAlgoritma) {
		this.vrstaAlgoritma = vrstaAlgoritma;
	}
	public TekstoviSluzbeni getTekstSluzbeni() {
		return tekstSluzbeni;
	}
	public void setTekstSluzbenit(TekstoviSluzbeni tekstSluzbeni) {
		this.tekstSluzbeni = tekstSluzbeni;
	}
	
	public static Map<VrstaAlgoritmaEnum, List<RezultatiPretrage>> findByKriterijPretrage(KriterijPretrage kriterijPretrage) {
		String kljucneRijeci = kriterijPretrage.getKljucneRijeci();
		String[] keywords = kljucneRijeci.replace(",", "").split(" ");
		
		Map<VrstaAlgoritmaEnum, List<RezultatiPretrage>> listeKljucnihRijeci = new HashMap<>();
		for (VrstaAlgoritmaEnum vrstaAlgoritma : VrstaAlgoritmaEnum.values()) {
			List<KljucneRijeci> list = Repositories.kljucneRijeciRepository.findByKljucneRijeciAndVrstaAlgoritma(kljucneRijeci, keywords, vrstaAlgoritma);
			
			listeKljucnihRijeci.put(vrstaAlgoritma, 
				list.stream().map(kr -> new RezultatiPretrage(vrstaAlgoritma, Repositories.tekstoviSluzbeniRepository.findById(kr.getTsiId()))).collect(Collectors.toList())
			);
		}
		
		return listeKljucnihRijeci;
	}
}
