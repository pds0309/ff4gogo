package com.pds.web.service;


import com.pds.openapi.api.Fifa4PlayerImgApi;
import com.pds.common.config.GenModelMapper;
import com.pds.common.dto.PlayerDto;
import com.pds.common.entity.Player;
import com.pds.common.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlayerResponseService {
    private final PlayerRepository playerRepository;
    private final GenModelMapper modelMapper;

    public PlayerDto.InfoWithImage getPlayer(int pId) {
        Player player = playerRepository.findById(pId).orElse(null);
        return new PlayerDto.InfoWithImage((PlayerDto.Info) modelMapper.entityToDto(
                (player!=null) ? player : playerRepository.findById(101111111).orElse(null),
                PlayerDto.Info.class), Fifa4PlayerImgApi.findPlayerImgUrl(pId));
    }
}
