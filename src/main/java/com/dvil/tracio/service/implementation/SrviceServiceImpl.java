package com.dvil.tracio.service.implementation;

import com.dvil.tracio.dto.SrviceDTO;
import com.dvil.tracio.entity.Srvice;
import com.dvil.tracio.mapper.SrviceMapper;
import com.dvil.tracio.repository.SrviceRepo;
import com.dvil.tracio.service.SrviceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SrviceServiceImpl implements SrviceService {
    private final SrviceRepo srviceRepo;
    private final SrviceMapper srviceMapper = SrviceMapper.INSTANCE;

    public SrviceServiceImpl(SrviceRepo srviceRepo) {
        this.srviceRepo = srviceRepo;
    }

    @Override
    public List<SrviceDTO> getAllSrvices() {
        return srviceRepo.findAll().stream()
                .map(srviceMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public SrviceDTO getSrviceById(Integer id) {
        Srvice srvice = srviceRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Srvice not found"));
        return srviceMapper.toDTO(srvice);
    }

    @Override
    @Transactional
    public SrviceDTO createSrvice(SrviceDTO srviceDTO) {
        Srvice srvice = srviceMapper.toEntity(srviceDTO);
        srvice.setCreatedAt(OffsetDateTime.now());
        srvice = srviceRepo.save(srvice);
        return srviceMapper.toDTO(srvice);
    }

    @Override
    @Transactional
    public SrviceDTO updateSrvice(Integer id, SrviceDTO srviceDTO) {
        Srvice existingSrvice = srviceRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Srvice not found"));

        existingSrvice.setServName(srviceDTO.getServName());
        existingSrvice.setServDescription(srviceDTO.getServDescription());

        srviceRepo.save(existingSrvice);
        return srviceMapper.toDTO(existingSrvice);
    }

    @Override
    @Transactional
    public void deleteSrvice(Integer id) {
        Srvice srvice = srviceRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Srvice not found"));
        srviceRepo.delete(srvice);
    }
}
