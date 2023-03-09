package com.kakao.apitest.youtube.controller;

import com.kakao.apitest.youtube.dto.YouTubeDto;
import com.kakao.apitest.youtube.service.YouTubeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class YouTubeController {

	@Autowired
	YouTubeService youTubeService;

	@RequestMapping(value = {"/youtube"}, method = RequestMethod.GET)
	public @ResponseBody List<YouTubeDto> searchYouTube(
			@RequestParam(value = "search") String search,
			@RequestParam(value = "items", required = false, defaultValue = "5") String items) {

		int max = Integer.parseInt(items);
		return youTubeService.youtubeSearch(search, max);
	}

}
