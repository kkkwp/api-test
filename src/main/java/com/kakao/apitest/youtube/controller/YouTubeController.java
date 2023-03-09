package com.kakao.apitest.youtube.controller;

import com.kakao.apitest.youtube.dto.YouTubeDto;
import com.kakao.apitest.youtube.service.YouTubeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class YouTubeController {

	private final YouTubeService youTubeService;

	@Autowired
	public YouTubeController(final YouTubeService youTubeService) {
		this.youTubeService = youTubeService;
	}

	@GetMapping("/youtube")
	public YouTubeDto Index() {
		return youTubeService.get();
	}

}
