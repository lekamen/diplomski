package hr.lenak.diplomski.core.model.generated;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import hr.lenak.diplomski.core.model.TipoviDokumenata;
import hr.lenak.diplomski.core.model.enums.DaNeEnum;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QTipoviDokumenata is a Querydsl query type for TipoviDokumenata
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QTipoviDokumenata extends EntityPathBase<TipoviDokumenata> {

    private static final long serialVersionUID = -25035399L;

    public static final QTipoviDokumenata tipoviDokumenata = new QTipoviDokumenata("tipoviDokumenata");

    public final EnumPath<DaNeEnum> tdaAktivan = createEnum("tdaAktivan", DaNeEnum.class);

    public final NumberPath<Long> tdaId = createNumber("tdaId", Long.class);

    public final StringPath tdaNaziv = createString("tdaNaziv");

    public final NumberPath<java.math.BigDecimal> tdaTip = createNumber("tdaTip", java.math.BigDecimal.class);

    public QTipoviDokumenata(String variable) {
        super(TipoviDokumenata.class, forVariable(variable));
    }

    public QTipoviDokumenata(Path<? extends TipoviDokumenata> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTipoviDokumenata(PathMetadata metadata) {
        super(TipoviDokumenata.class, metadata);
    }

}

