package com.kakao.apitest.youtube.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class YouTubeDto {
	private String url; // 영상 url
	private String title; // 영상 제목
	private String thumbnail; // 영상 썸네일 경로
	private String description; // 영상 설명

	@Builder
	public YouTubeDto(String url, String title, String thumbnail, String description) {
		this.url = url;
		this.title = title;
		this.thumbnail = thumbnail;
		this.description = description;
	}
}
