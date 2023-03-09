package com.kakao.apitest.youtube.service;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Thumbnail;
import com.google.api.services.youtube.model.Video;
import com.kakao.apitest.youtube.dto.YouTubeDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

@Service
public class YouTubeImpl implements YouTubeService {

	@Value("${youtube-apiKey}")
	private String API_KEY;
	@Value("${youtube-videoId}")
	private String VIDEO_ID;

	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	private static final JsonFactory JSON_FACTORY = new JacksonFactory();
	private static final long NUMBER_OF_VIDEOS_RETURNED = 1;

	/*
	 * 영상 id, 제목, 영상 길이, 썸네일 링크 출력
	 */
	private static void prettyPrint(Iterator<Video> iteratorSearchResults, YouTubeDto youTubeDto) {

		System.out.println("\n=============================================================");
		System.out.println("=============================================================\n");

		// 검색 결과 없다면
		if (!iteratorSearchResults.hasNext()) {
			System.out.println(" 검색 결과 없음");
		}

		// 검색 결과 있다면 모두 출력
		while (iteratorSearchResults.hasNext()) {
			Video video = iteratorSearchResults.next();

			// 비디오 종류를 더블 체크해서 정보 가져오기
			if (video.getKind().equals("youtube#video")) {

				Thumbnail thumbnail = (Thumbnail) video.getSnippet().getThumbnails().get("default");

				System.out.println(" 영상 id: " + video.getId());
				System.out.println(" 제목: " + video.getSnippet().getTitle());
				System.out.println(" 영상 길이: " + video.getContentDetails().getDuration());
				System.out.println(" 썸네일: " + thumbnail.getUrl());
				System.out.println("\n-------------------------------------------------------------\n");

				youTubeDto.setThumbnailPath(thumbnail.getUrl());
				youTubeDto.setTitle(video.getSnippet().getTitle());
				youTubeDto.setVideoId(video.getId());
			}
		}
	}

	@Override
	public YouTubeDto get() {

		YouTubeDto youTubeDto = new YouTubeDto();

		try {
			YouTube youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, request -> {})
					.setApplicationName("youtube-video-duration-get")
					.build();

			// 내가 원하는 정보 지정
			YouTube.Videos.List videos = youtube.videos().list("id, snippet, contentDetails");
			videos.setKey(API_KEY); // api key
			videos.setId(VIDEO_ID); // video id (얘는 hype boy)
			videos.setMaxResults(NUMBER_OF_VIDEOS_RETURNED); // 최대 조회 갯수

			List<Video> videosList = videos.execute().getItems();
			if (videosList != null) {
				prettyPrint(videosList.iterator(), youTubeDto);
			}
		} catch (GoogleJsonResponseException e) {
			System.out.println("서비스 에러: " + e.getDetails().getCode() + " : " + e.getDetails().getMessage());
		} catch (IOException e) {
			System.out.println("IO 에러: " + e.getCause() + " : " + e.getMessage());
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return youTubeDto;
	}

}
