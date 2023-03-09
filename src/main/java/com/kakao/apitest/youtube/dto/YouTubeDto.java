package com.kakao.apitest.youtube.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class YouTubeDto {

	private String title; // 영상 제목
	private String thumbnailPath; // 영상 썸네일 경로
	private String videoId; // 영상 식별 ID

}
