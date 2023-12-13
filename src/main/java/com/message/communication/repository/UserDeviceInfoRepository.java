package com.message.communication.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.message.communication.entity.UserDeviceInfo;



public interface UserDeviceInfoRepository extends JpaRepository<UserDeviceInfo, Long>{
   public List<UserDeviceInfo> findByUseridOrderByDeviceinfoidDesc(Long userid);
}
