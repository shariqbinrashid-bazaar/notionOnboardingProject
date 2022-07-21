package com.bazaar.api.template

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.yml")
class BazaarTemplateApplicationTest {


    @Test
    fun contextLoads() {
    }
}