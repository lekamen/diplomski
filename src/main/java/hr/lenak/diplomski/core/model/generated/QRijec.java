package hr.lenak.diplomski.core.model.generated;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import hr.lenak.diplomski.core.model.Rijec;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QRijec is a Querydsl query type for Rijec
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QRijec extends EntityPathBase<Rijec> {

    private static final long serialVersionUID = -1504917172L;

    public static final QRijec rijec = new QRijec("rijec");

    public final NumberPath<Double> idf = createNumber("idf", Double.class);

    public final NumberPath<Integer> pojaveUDokumentu = createNumber("pojaveUDokumentu", Integer.class);

    public final NumberPath<Integer> pojaveUKorpusu = createNumber("pojaveUKorpusu", Integer.class);

    public final NumberPath<Double> result = createNumber("result", Double.class);

    public final NumberPath<Long> tekstZakonaId = createNumber("tekstZakonaId", Long.class);

    public final NumberPath<Double> tf = createNumber("tf", Double.class);

    public final NumberPath<Long> tokenId = createNumber("tokenId", Long.class);

    public QRijec(String variable) {
        super(Rijec.class, forVariable(variable));
    }

    public QRijec(Path<? extends Rijec> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRijec(PathMetadata metadata) {
        super(Rijec.class, metadata);
    }

}

