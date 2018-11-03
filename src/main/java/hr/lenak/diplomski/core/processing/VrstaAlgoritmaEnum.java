package hr.lenak.diplomski.core.processing;

public enum VrstaAlgoritmaEnum {
	TEXTRANK,
	TEXTRANKMULWIN,
	TFIDF,
	TEXTRANKIDF,
	TEXTRANKMULWINIDF;
	
	public static String getName(VrstaAlgoritmaEnum vrsta) {
		switch(vrsta) {
		case TEXTRANK:
			return "TextRank algoritam";
		case TEXTRANKMULWIN:
			return "TextRankMulWin algoritam";
		case TFIDF:
			return "tf-idf algoritam";
		case TEXTRANKIDF:
			return "TextRank-idf algoritam";
		case TEXTRANKMULWINIDF:
			return "TextRankMulWin-idf algoritam";
		}
		return null;
	}
}
