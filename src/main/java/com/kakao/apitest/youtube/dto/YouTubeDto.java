package com.kakao.apitest.youtube.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class YouTubeDto {

	private String id; // 영상 id
	private String title; // 영상 제목
	private String thumbnail; // 영상 썸네일 경로

	@Builder
	public YouTubeDto(String id, String title, String thumbnail) {
		this.id = id;
		this.title = title;
		this.thumbnail = thumbnail;
	}
}
