package com.kakao.apitest.youtube.service;

import com.kakao.apitest.youtube.dto.YouTubeDto;

import java.util.List;

public interface YouTubeService {

	List<YouTubeDto> youtubeSearch(String searchQuery, int maxSearch);

}
