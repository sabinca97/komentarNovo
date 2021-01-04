package si.fri.rso.samples.imagecatalog.models.converters;

import si.fri.rso.samples.imagecatalog.lib.KomentarMetadata;
import si.fri.rso.samples.imagecatalog.models.entities.KomentarMetadataEntity;

public class KomentarMetadataConverter {

    public static KomentarMetadata toDto(KomentarMetadataEntity entity) {

        KomentarMetadata dto = new KomentarMetadata();
        dto.setVsebina(entity.getVsebina());
        dto.setImage_id(entity.getImage_id());
        dto.setId(entity.getId());
        return dto;

    }

    public static KomentarMetadataEntity toEntity(KomentarMetadata dto) {

        KomentarMetadataEntity entity = new KomentarMetadataEntity();
        entity.setVsebina(dto.getVsebina());
        entity.setImage_id(dto.getImage_id());
        entity.setId(dto.getId());
        return entity;

    }

}
