package com.dvil.tracio.service;

import com.dvil.tracio.dto.SrviceDTO;
import java.util.List;

public interface SrviceService {
    List<SrviceDTO> getAllServices();
    SrviceDTO getServiceById(Integer id);
    SrviceDTO createService(SrviceDTO srviceDTO);
    SrviceDTO updateService(Integer id, SrviceDTO srviceDTO);
    void deleteService(Integer id);
}
