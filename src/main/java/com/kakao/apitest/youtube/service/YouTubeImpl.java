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
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

@Log4j2
@Service
public class YouTubeImpl implements YouTubeService {

	@Value("${youtube-apiKey}")
	private String API_KEY;
	@Value("${youtube-fields}")
	private String SEARCH_FIELDS;

	private static final String API_APPLICATION = "google-youtube-api-search";

	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	private static final JsonFactory JSON_FACTORY = new JacksonFactory();
	private static final long NUMBER_OF_VIDEOS_RETURNED = 1;
	private static YouTube youTube;

	// 검색 결과(video id, title, duration, thumbnail)를 출력
	private static void prettyPrint(Iterator<Video> iteratorSearchResults, YouTubeDto youTubeDto) {

		System.out.println("\n=============================================================");
		System.out.println("=============================================================\n");

		// 검색 결과가 없는 경우
		if (!iteratorSearchResults.hasNext()) {
			log.warn(" 검색 결과 없음!");
		}

		// 검색 결과가 있는 경우
		while (iteratorSearchResults.hasNext()) {

			Video singleVideo = iteratorSearchResults.next();

			// 검색하려는 게 video이라는 걸 더블 체크
			if (singleVideo.getKind().equals("youtube#video")) {

				// 정보 가져오기
				Thumbnail thumbnail = (Thumbnail) singleVideo.getSnippet().getThumbnails().get("default");
				youTubeDto.setId(singleVideo.getId());
				youTubeDto.setTitle(singleVideo.getSnippet().getTitle());
				youTubeDto.setThumbnail(thumbnail.getUrl());

				// 출력
				System.out.println(" video id: " + singleVideo.getId());
				System.out.println(" title: " + singleVideo.getSnippet().getTitle());
				System.out.println(" Thumbnail: " + thumbnail.getUrl());
				System.out.println("\n-------------------------------------------------------------\n");
			}
		}
	}

	// video id를 가지고 내가 원하는 정보 가져오기
	@Override
	public YouTubeDto get(String videoId) {

		log.info("유튜브 탐색 시작... " + videoId);

		YouTubeDto YouTubeDto = new YouTubeDto();

		try {
			// youtube data api 요청 생성
			youTube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, request -> {})
					.setApplicationName(API_APPLICATION)
					.build();

			// 검색 결과를 위한 api request 정의
			YouTube.Videos.List videos = youTube.videos().list("id,snippet");
			videos.setKey(API_KEY);
			videos.setId(videoId);
			//videos.setFields(SEARCH_FIELDS); // api에서 정해놓은 field만 이용하여 검색함으로써 검색 효율을 높임
			videos.setMaxResults(NUMBER_OF_VIDEOS_RETURNED); // 최대 조회 개수
			List<Video> videoList = videos.execute().getItems();

			if (videoList != null) {
				prettyPrint(videoList.iterator(), YouTubeDto);
			}

		} catch (GoogleJsonResponseException e) {
			log.warn("서비스 에러: " + e.getDetails().getCode() + " : " + e.getDetails().getMessage());
		} catch (IOException e) {
			log.warn("IO 에러: " + e.getCause() + " : " + e.getMessage());
		} catch (Throwable t) {
			log.warn("서버 에러", t);
			t.printStackTrace();
		}

		return YouTubeDto;
	}

}
