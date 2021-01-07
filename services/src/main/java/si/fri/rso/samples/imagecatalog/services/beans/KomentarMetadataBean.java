package si.fri.rso.samples.imagecatalog.services.beans;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import org.eclipse.microprofile.metrics.annotation.Timed;
import si.fri.rso.samples.imagecatalog.lib.KomentarMetadata;
import si.fri.rso.samples.imagecatalog.models.converters.KomentarMetadataConverter;
import si.fri.rso.samples.imagecatalog.models.entities.KomentarMetadataEntity;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;


@RequestScoped
public class KomentarMetadataBean {

    private Logger log = Logger.getLogger(KomentarMetadataBean.class.getName());

    @Inject
    private EntityManager em;

    public List<KomentarMetadata> getKomentarMetadata() {

        TypedQuery<KomentarMetadataEntity> query = em.createNamedQuery(
                "KomentarMetadataEntity.getAll", KomentarMetadataEntity.class);

        List<KomentarMetadataEntity> resultList = query.getResultList();

        return resultList.stream().map(KomentarMetadataConverter::toDto).collect(Collectors.toList());

    }

    public List<KomentarMetadata> getKomentarMetadataFilter(UriInfo uriInfo) {

        int count = 0;
        int maxTries = 5;
        while(true) {
            try {
                QueryParameters queryParameters = QueryParameters.query(uriInfo.getRequestUri().getQuery()).defaultOffset(0)
                        .build();

                return JPAUtils.queryEntities(em, KomentarMetadataEntity.class, queryParameters).stream()
                        .map(KomentarMetadataConverter::toDto).collect(Collectors.toList());
            } catch (Exception e) {
                // handle exception
                if (++count == maxTries) throw e;
            }

        }




    }

    @Timed(name = "timer koliko casa rabi za pridobit slike")
    public KomentarMetadata getKomentarMetadata(Integer id) {

        KomentarMetadataEntity komentarMetadataEntity = em.find(KomentarMetadataEntity.class, id);

        if (komentarMetadataEntity == null) {
            throw new NotFoundException();
        }

        KomentarMetadata komentarMetadata = KomentarMetadataConverter.toDto(komentarMetadataEntity);

        return komentarMetadata;
    }

    public KomentarMetadata createKomentarMetadata(KomentarMetadata komentarMetadata) {

        KomentarMetadataEntity komentarMetadataEntity = KomentarMetadataConverter.toEntity(komentarMetadata);

        try {
            beginTx();
            em.persist(komentarMetadataEntity);
            commitTx();
        }
        catch (Exception e) {
            rollbackTx();
        }

        if (komentarMetadataEntity.getId() == null) {
            throw new RuntimeException("Entity was not persisted");
        }

        return KomentarMetadataConverter.toDto(komentarMetadataEntity);
    }


    public List<KomentarMetadata> getKomentarMetadataByImageId(Integer image_id) {

        List<KomentarMetadataEntity> resultList = em.createNamedQuery("KomentarMetadataEntity.getbyImageId", KomentarMetadataEntity.class)
                .setParameter("image_id", image_id)
                .getResultList();

        return resultList.stream().map(KomentarMetadataConverter::toDto).collect(Collectors.toList());

    }

    public boolean deleteImageMetadata(Integer id) {

        KomentarMetadataEntity imageMetadata = em.find(KomentarMetadataEntity.class, id);

        if (imageMetadata != null) {
            try {
                beginTx();
                em.remove(imageMetadata);
                commitTx();
            }
            catch (Exception e) {
                rollbackTx();
            }
        }
        else {
            return false;
        }

        return true;
    }

    private void beginTx() {
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
    }

    private void commitTx() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().commit();
        }
    }

    private void rollbackTx() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
    }
}
