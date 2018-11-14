package com.mystore.store.repository;

import com.mystore.store.model.Image;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly=true)
@NamedQueries({
        @NamedQuery(name="ImageRepository.findByProductId",
                query="SELECT image FROM Image image WHERE image.product.id = :productId")
})
public interface ImageRepository extends JpaRepository<Image, Long> {
        List<Image> findByProductId(@Param("productId") Long productId);
}
