    package com.dvil.tracio.service.implementation;

    import com.dvil.tracio.dto.SrviceDTO;
    import com.dvil.tracio.entity.Shop;
    import com.dvil.tracio.entity.ShopService;
    import com.dvil.tracio.entity.Srvice;
    import com.dvil.tracio.entity.User;
    import com.dvil.tracio.enums.RoleName;
    import com.dvil.tracio.mapper.SrviceMapper;
    import com.dvil.tracio.repository.ShopRepo;
    import com.dvil.tracio.repository.ShopServiceRepo;
    import com.dvil.tracio.repository.SrviceRepo;
    import com.dvil.tracio.repository.UserRepo;
    import com.dvil.tracio.service.SrviceService;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.http.HttpStatus;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;
    import org.springframework.web.server.ResponseStatusException;

    import java.time.OffsetDateTime;
    import java.util.List;
    import java.util.stream.Collectors;

    @Service
    public class SrviceServiceImpl implements SrviceService {

        private static final Logger logger = LoggerFactory.getLogger(SrviceServiceImpl.class);

        private final SrviceRepo srviceRepo;
        private final ShopRepo shopRepo;
        private final ShopServiceRepo shopServiceRepo;
        private final UserRepo userRepo;
        private final SrviceMapper srviceMapper = SrviceMapper.INSTANCE;

        public SrviceServiceImpl(SrviceRepo srviceRepo, ShopRepo shopRepo, ShopServiceRepo shopServiceRepo, UserRepo userRepo) {
            this.srviceRepo = srviceRepo;
            this.shopRepo = shopRepo;
            this.shopServiceRepo = shopServiceRepo;
            this.userRepo = userRepo;
        }

        @Override
        public List<SrviceDTO> getAllServices() {
            return srviceRepo.findAll().stream()
                    .map(srviceMapper::toDTO)
                    .collect(Collectors.toList());
        }

        @Override
        public SrviceDTO getServiceById(Integer id) {
            Srvice srvice = srviceRepo.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dịch vụ với ID " + id + " không tồn tại"));
            return srviceMapper.toDTO(srvice);
        }

        @Override
        @Transactional
        public SrviceDTO createService(SrviceDTO srviceDTO, Shop shop) {
            try {
                logger.info("Creating new service for shop: " + shop.getId());

                // Kiểm tra nếu service đã tồn tại
                Srvice srvice = srviceMapper.toEntity(srviceDTO);
                srvice.setCreatedAt(OffsetDateTime.now());
                srvice.setServName(srviceDTO.getServType());
                srvice.setServDescription(srviceDTO.getServDescription());

                logger.info("Saving service: " + srvice.getServDescription());

                // Lưu service
                Srvice savedService = srviceRepo.save(srvice);
                srviceRepo.flush();  // Đảm bảo dữ liệu được lưu ngay lập tức

                // Kiểm tra xem service đã được lưu chưa
                if (savedService == null || savedService.getId() == null) {
                    throw new RuntimeException("Service save failed");
                }

                logger.info("Service saved: " + savedService.getId());

                // Liên kết service với shop
                ShopService shopService = new ShopService();
                shopService.setCreatedAt(OffsetDateTime.now());
                shopService.setService(savedService);
                shopService.setShop(shop);
                shopServiceRepo.save(shopService);
                shopServiceRepo.flush(); // Đảm bảo lưu ngay lập tức

                logger.info("ShopService saved successfully");

                return srviceMapper.toDTO(savedService);
            } catch (Exception e) {
                logger.error("Lỗi khi lưu service: ", e);
                throw new RuntimeException("Lỗi khi tạo service: " + e.getMessage());
            }
        }

        @Override
        @Transactional
        public SrviceDTO updateService(Integer id, SrviceDTO srviceDTO) {
            User user = getCurrentUser();

            Srvice existing = srviceRepo.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dịch vụ với ID " + id + " không tồn tại"));

            boolean isAdmin = user.getRole().equals(RoleName.ADMIN);
            boolean isShopOwner = user.getRole().equals(RoleName.SHOP_OWNER);

            if (!isAdmin && !isShopOwner) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền cập nhật dịch vụ này");
            }

            existing.setServName(srviceDTO.getServType());
            existing.setServDescription(srviceDTO.getServDescription());

            return srviceMapper.toDTO(srviceRepo.save(existing));
        }

        @Override
        @Transactional
        public void deleteService(Integer id) {
            User user = getCurrentUser();

            Srvice srvice = srviceRepo.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dịch vụ với ID " + id + " không tồn tại"));

            boolean isAdmin = user.getRole().equals(RoleName.ADMIN);
            boolean isShopOwner = user.getRole().equals(RoleName.SHOP_OWNER);

            if (!isAdmin && !isShopOwner) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền xoá dịch vụ này");
            }

            srviceRepo.delete(srvice);
        }
        private User getCurrentUser() {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof UserDetails) {
                String username = ((UserDetails) principal).getUsername();
                return userRepo.findByUsername(username)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Không tìm thấy người dùng"));
            }
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Không thể xác thực người dùng");
        }

    }
