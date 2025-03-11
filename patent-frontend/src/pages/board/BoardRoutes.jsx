// src/pages/board/BoardRoutes.jsx
import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import BoardLayout from '../../components/layout/BoardLayout';

import BoardList from './BoardList';
import BoardDetail from './BoardDetail';
import BoardWrite from './BoardWrite';
import BoardEdit from './BoardEdit';

function BoardRoutes() {
  return (
    <Routes>
      {/*
        /board → BoardLayout 적용
        왼쪽 사이드메뉴 + 오른쪽 게시판 페이지
      */}
      <Route element={<BoardLayout />}>
        {/* /board 자체로 들어오면 /board/notice로 리다이렉트 */}
        <Route index element={<Navigate to="notice" replace />} />

        {/* 공지사항 */}
        <Route path="notice" element={<BoardList category="NOTICE" />} />
        <Route path="notice/:id" element={<BoardDetail category="NOTICE" />} />
        <Route path="notice/:id/edit" element={<BoardEdit category="NOTICE" />} />
        <Route path="notice/write" element={<BoardWrite category="NOTICE" />} />

        {/* 질문/건의 */}
        <Route path="qna" element={<BoardList category="QNA" />} />
        <Route path="qna/:id" element={<BoardDetail category="QNA" />} />
        <Route path="qna/:id/edit" element={<BoardEdit category="QNA" />} />
        <Route path="qna/write" element={<BoardWrite category="QNA" />} />

        {/* 자유게시판 */}
        <Route path="free" element={<BoardList category="FREE" />} />
        <Route path="free/:id" element={<BoardDetail category="FREE" />} />
        <Route path="free/:id/edit" element={<BoardEdit category="FREE" />} />
        <Route path="free/write" element={<BoardWrite category="FREE" />} />

        {/* 구인/구직 (비공개) */}
        <Route path="job" element={<BoardList category="JOB" />} />
        <Route path="job/:id" element={<BoardDetail category="JOB" />} />
        <Route path="job/:id/edit" element={<BoardEdit category="JOB" />} />
        <Route path="job/write" element={<BoardWrite category="JOB" />} />
      </Route>
    </Routes>
  );
}

export default BoardRoutes;
