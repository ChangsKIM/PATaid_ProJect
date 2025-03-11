// src/pages/board/BoardSideMenu.jsx
import React from 'react';
import { Link } from 'react-router-dom';
import '../../css/BoardSideMenu.css';

function BoardSideMenu() {
  return (
    <div className="board-side-menu">
      <h3>게시판 목록</h3>
      <ul>
        <li><Link to="/board/notice">공지사항</Link></li>
        <li><Link to="/board/qna">질문 및 건의</Link></li>
        <li><Link to="/board/free">자유 게시판</Link></li>
        <li><Link to="/board/job">구인/구직(비공개)</Link></li>
      </ul>
    </div>
  );
}

export default BoardSideMenu;
