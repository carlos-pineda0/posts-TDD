package com.example.pineda.post;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
@AutoConfigureMockMvc
public class PostControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PostRepository postRepository;

    List<Post> posts = new ArrayList<>();

    @BeforeEach
    void setUp() {
        posts = List.of(
                new Post(1, 1, "Hello, World!", "This is my first post.", null),
                new Post(2, 1, "Second Post", "This is my second post.", null)
        );
    }

    // REST API

    // list
    @Test
    void shouldFindAllPosts() throws Exception {

        String jsonResponse = """
                [
                    {
                        "id":1,
                        "userId":1,
                        "title":"Hello, World!",
                        "body":"This is my first post.",
                        "version": null
                    },
                    {
                        "id":2,
                        "userId":1,
                        "title":"Second Post",
                        "body":"This is my second post.",
                        "version": null
                    }
                ]
                """;

        when(
                postRepository.findAll()
        ).thenReturn(posts);

        mockMvc.perform(
                get("/api/posts")
        ).andExpect(
                status().isOk()
        ).andExpect(
                content().json(jsonResponse)
        );
    }


    // /api/posts/1
    @Test
    void shouldFindPostWhenGivenValidId() throws Exception {


        when(
                postRepository.findById(1)
        ).thenReturn(
                Optional.of(posts.get(0))
        );

        var post = posts.get(0);
        // need to run this with --enable-preview for String Template to work
//        var json = STR."""
//                {
//                    "id":\{post.id()},
//                    "userId":\{post.userId()},
//                    "title":"\{post.title()}",
//                    "body":"\{post.body()}",
//                    "version": null
//                }
//        """;
        var json = """
                {
                    "body": "This is my first post.",
                    "id": 1,
                    "title": "Hello, World!",
                    "userId": 1,
                    "version": null
                }
                                
                """;


        mockMvc.perform(
                get("/api/posts/1")
        ).andExpect(
                status().isOk()
        ).andExpect(
                content().json(json)
        );
    }


    // /api/posts/999
    @Test
    void shouldNotFindPostWhenGivenInvalidID() throws Exception {
        when(postRepository.findById(999)).thenThrow(PostNotFoundException.class);

        mockMvc.perform(
                get("/api/posts/999")
        ).andExpect(
                status().isNotFound()
        );
    }

}
