package com.example.counter.repository;

import com.example.counter.entiry.User;
import com.example.counter.service.CategoryService;
import com.example.counter.service.ExpansesService;
import com.example.counter.service.UserService;
import com.example.counter.util.DefaultCategoriesCreator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminUserLoader implements ApplicationRunner {

    private final UserService userService;
    private final ExpansesService expansesService;
    private final CategoryService categoryService;
    private DefaultCategoriesCreator creator = new DefaultCategoriesCreator();
    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        User user = userService.registerAdminUser();
//        List<Category> categories = categoryService.createCategoriesAndAttachToUser(user, true);
//        user.setCategories(categories);
        user.setCategories(creator.createAdminCategories());
        userService.updateUser(user);
        expansesService.createTestDataInDB(user);
    }
}
