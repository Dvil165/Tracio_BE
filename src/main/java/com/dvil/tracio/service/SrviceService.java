package com.dvil.tracio.service;

import com.dvil.tracio.dto.SrviceDTO;
import com.dvil.tracio.entity.Shop;

import java.util.List;

public interface SrviceService {
    List<SrviceDTO> getAllServices();
    SrviceDTO getServiceById(Integer id);
    SrviceDTO createService(SrviceDTO srviceDTO, Shop shop);
    SrviceDTO updateService(Integer id, SrviceDTO srviceDTO);
    void deleteService(Integer id);
}
