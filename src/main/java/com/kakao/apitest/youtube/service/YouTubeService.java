package com.kakao.apitest.youtube.service;

import com.kakao.apitest.youtube.dto.YouTubeDto;


public interface YouTubeService {

	YouTubeDto get(String videoId);

}
