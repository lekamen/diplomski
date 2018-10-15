package hr.lenak.diplomski.core.processing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GrafUtils {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	public void makniNepovezaneVrhove(Graf graf) {
		HashMap<Vrh, Boolean> visited = new HashMap<>();
		HashSet<Vrh> vrhovi = graf.getVrhovi();
		for (Vrh vrh : vrhovi) {
			visited.put(vrh, false);
		}
		dfs(vrhovi.iterator().next(), visited, graf);
		List<Vrh> nepovezani = nadjiNepovezaneVrhove(graf, visited);
		for (Vrh v : nepovezani) {
			log.debug("nepovezani vrhovi " + v);
		}
		makniNepovezaneVrhove(graf, nepovezani);
	}
	
	private void dfs(Vrh vrh, HashMap<Vrh, Boolean> visited, Graf graf){
		if (visited.get(vrh)) {
			return;
		}
		visited.put(vrh, true);
		for (Vrh v : graf.nadjiSusjedeZaVrh(vrh)) {
			if (visited.get(v).equals(false)) {
				dfs(v, visited, graf);
			}
		}
	}
	
	private void makniNepovezaneVrhove(Graf graf, List<Vrh> nepovezani) {
		for (Vrh v : nepovezani) {
			graf.ukloniVrhIBridove(v);
		}
	}
	 
	private List<Vrh> nadjiNepovezaneVrhove(Graf graf, HashMap<Vrh, Boolean> visited){
		List<Vrh> nepovezaniVrhovi = new ArrayList<>();
		for(Vrh v : graf.getVrhovi()) {
			if (visited.get(v) == false) {
				log.debug("vrh " + v + " nije povezan");
				nepovezaniVrhovi.add(v);
			}
		}
		return nepovezaniVrhovi;
	}
}


