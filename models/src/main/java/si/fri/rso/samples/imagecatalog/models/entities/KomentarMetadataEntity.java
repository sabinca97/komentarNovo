package si.fri.rso.samples.imagecatalog.models.entities;

import javax.persistence.*;

@Entity
@Table(name = "komentar_metadata")
@NamedQueries(value =
        {
                @NamedQuery(name = "KomentarMetadataEntity.getAll",
                        query = "SELECT im FROM KomentarMetadataEntity im"),

                @NamedQuery(
                        name = "KomentarMetadataEntity.getbyImageId",
                        query = "Select im from KomentarMetadataEntity im where im.image_id = :image_id"
                )

        })
public class KomentarMetadataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "vsebina")
    private String vsebina;

    @Column(name = "image_id")
    private Integer image_id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVsebina() {
        return vsebina;
    }

    public void setVsebina(String vsebina) {
        this.vsebina = vsebina;
    }

    public Integer getImage_id() {
        return image_id;
    }

    public void setImage_id(Integer image_id) {
        this.image_id = image_id;
    }
}