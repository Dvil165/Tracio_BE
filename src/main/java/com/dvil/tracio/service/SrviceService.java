package com.dvil.tracio.service;

import com.dvil.tracio.dto.SrviceDTO;
import java.util.List;

public interface SrviceService {
    List<SrviceDTO> getAllSrvices();
    SrviceDTO getSrviceById(Integer id);
    SrviceDTO createSrvice(SrviceDTO srviceDTO);
    SrviceDTO updateSrvice(Integer id, SrviceDTO srviceDTO);
    void deleteSrvice(Integer id);
}
