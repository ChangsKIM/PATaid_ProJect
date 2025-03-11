// src/components/layout/BoardLayout.jsx
import React from 'react';
import { Outlet } from 'react-router-dom';
import BoardSideMenu from '../../pages/board/BoardSideMenu';
import Header from './Header';
import Footer from './Footer';
import '../../css/BoardLayout.css'; // 필요한 경우 CSS

function BoardLayout() {
  return (
    <div className="board-layout">
      {/* 시나리오 상단부(header) */}
      <Header />

      <div className="board-container">
        {/* 왼쪽 영역: 게시판 이동 링크 */}
        <BoardSideMenu />

        {/* 오른쪽 영역: 실제 게시판 페이지 */}
        <div className="board-content">
          <Outlet />
        </div>
      </div>

      <Footer />
    </div>
  );
}

export default BoardLayout;
