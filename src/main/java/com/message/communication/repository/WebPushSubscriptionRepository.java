package com.message.communication.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.message.communication.entity.WebPushSubscription;





public interface WebPushSubscriptionRepository extends JpaRepository<WebPushSubscription, Long>{

}
