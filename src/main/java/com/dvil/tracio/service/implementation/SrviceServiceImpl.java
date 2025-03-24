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
        public SrviceDTO createService(SrviceDTO srviceDTO) {
            User user = getCurrentUser();

            if (!user.getRole().equals(RoleName.SHOP_OWNER)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Chỉ Shop Owner mới được tạo dịch vụ");
            }

            List<Shop> shops = shopRepo.findByOwnerId(user.getId());
            if (shops.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bạn chưa có shop nào. Vui lòng tạo shop trước khi thêm dịch vụ.");
            }

            Shop shop = shops.get(0);

            Srvice srvice = srviceMapper.toEntity(srviceDTO);
            srvice.setCreatedAt(OffsetDateTime.now());
            Srvice savedService = srviceRepo.save(srvice);

            ShopService shopService = new ShopService();
            shopService.setCreatedAt(OffsetDateTime.now());
            shopService.setService(savedService);
            shopService.setShop(shop);
            shopServiceRepo.save(shopService);

            return srviceMapper.toDTO(savedService);
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
