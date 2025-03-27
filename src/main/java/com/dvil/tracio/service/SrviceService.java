package com.dvil.tracio.service;

import com.dvil.tracio.dto.SrviceDTO;
import com.dvil.tracio.entity.Shop;
import com.dvil.tracio.entity.User;

import java.util.List;

public interface SrviceService {
    List<SrviceDTO> getAllServices();
    List<SrviceDTO> getAllServicesOfAShop(User user);
    SrviceDTO getServiceById(Integer id);
    SrviceDTO createService(SrviceDTO srviceDTO, Shop shop);
    SrviceDTO updateService(Integer id, SrviceDTO srviceDTO);
    void deleteService(Integer id);
}
