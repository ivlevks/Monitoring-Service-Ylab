package org.ivlevks.domain.mappers;

import org.ivlevks.domain.dto.IndicationDto;
import org.ivlevks.domain.entity.Indication;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface IndicationsMapper {
    IndicationsMapper INSTANCE = Mappers.getMapper(IndicationsMapper.class);

    IndicationDto toIndicationDto(Indication indication);

    Indication toIndication(IndicationDto indicationDto);

}
