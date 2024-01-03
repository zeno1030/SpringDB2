package hello.itemservice.repository.jpa;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
@RequiredArgsConstructor
public class JpaItemRepositoryV2 implements ItemRepository {

    private final SpringDataJpaRepository springDataJpaRepository;
    @Override
    public Item save(Item item) {
        return springDataJpaRepository.save(item);
    }

    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        Item findItem = springDataJpaRepository.findById(itemId).orElseThrow();
        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());

    }

    @Override
    public Optional<Item> findById(Long id) {
        return springDataJpaRepository.findById(id);
    }

    @Override
    public List<Item> findAll(ItemSearchCond cond) {
        String itemName = cond.getItemName();
        Integer maxPrice = cond.getMaxPrice();

        if (StringUtils.hasText(itemName) && maxPrice != null){
            return springDataJpaRepository.findByItemNameLikeAndPriceLessThanEqual("%" + itemName + "%", maxPrice);
        } else if (StringUtils.hasText(itemName)) {
            return springDataJpaRepository.findByItemNameLike("%" + itemName + "%");
        } else if (maxPrice != null) {
            return springDataJpaRepository.findByPriceLessThanEqual(maxPrice);
       } else {
            return springDataJpaRepository.findAll();
        }
    }
}
