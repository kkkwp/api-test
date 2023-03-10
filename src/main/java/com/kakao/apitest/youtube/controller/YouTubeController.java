package com.kakao.apitest.youtube.controller;

import com.kakao.apitest.youtube.dto.YouTubeDto;
import com.kakao.apitest.youtube.service.YouTubeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/games")
public class YouTubeController {

	@Autowired
	YouTubeService youTubeService;

	@RequestMapping(value = {"/youtube"}, method = RequestMethod.GET)
	public @ResponseBody YouTubeDto searchYouTube(
			@RequestParam(value = "videoId") String videoId) {

		return youTubeService.get(videoId);
	}

}
