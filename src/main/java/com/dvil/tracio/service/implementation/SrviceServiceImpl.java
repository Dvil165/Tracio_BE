package com.dvil.tracio.service.implementation;

import com.dvil.tracio.dto.SrviceDTO;
import com.dvil.tracio.entity.Srvice;
import com.dvil.tracio.mapper.SrviceMapper;
import com.dvil.tracio.repository.SrviceRepo;
import com.dvil.tracio.service.SrviceService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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
    public List<SrviceDTO> getAllServices() {
        List<SrviceDTO> services = srviceRepo.findAll().stream()
                .map(srviceMapper::toDTO)
                .collect(Collectors.toList());

        if (services.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không có dịch vụ nào trong hệ thống");
        }
        return services;
    }

    @Override
    public SrviceDTO getServiceById(Integer id) {
        Srvice srvice = srviceRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dịch vụ với ID " + id + " không tồn tại"));

        return srviceMapper.toDTO(srvice);
    }

    @Override
    @Transactional
    public SrviceDTO createService(SrviceDTO srviceDTO) {
        Srvice srvice = srviceMapper.toEntity(srviceDTO);
        srvice.setCreatedAt(OffsetDateTime.now());

        return srviceMapper.toDTO(srviceRepo.save(srvice));
    }

    @Override
    @Transactional
    public SrviceDTO updateService(Integer id, SrviceDTO srviceDTO) {
        Srvice existingSrvice = srviceRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dịch vụ với ID " + id + " không tồn tại"));

        existingSrvice.setServName(srviceDTO.getServName());
        existingSrvice.setServDescription(srviceDTO.getServDescription());

        return srviceMapper.toDTO(srviceRepo.save(existingSrvice));
    }

    @Override
    @Transactional
    public void deleteService(Integer id) {
        Srvice srvice = srviceRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dịch vụ với ID " + id + " không tồn tại"));

        srviceRepo.delete(srvice);
    }
}
