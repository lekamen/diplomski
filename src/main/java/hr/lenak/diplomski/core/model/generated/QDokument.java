package hr.lenak.diplomski.core.model.generated;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import hr.lenak.diplomski.core.model.Dokument;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QDokument is a Querydsl query type for Dokument
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QDokument extends EntityPathBase<Dokument> {

    private static final long serialVersionUID = 325482456L;

    public static final QDokument dokument = new QDokument("dokument");

    public final NumberPath<Long> najcescaRijecId = createNumber("najcescaRijecId", Long.class);

    public final NumberPath<Long> tekstZakonaId = createNumber("tekstZakonaId", Long.class);

    public QDokument(String variable) {
        super(Dokument.class, forVariable(variable));
    }

    public QDokument(Path<? extends Dokument> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDokument(PathMetadata metadata) {
        super(Dokument.class, metadata);
    }

}

