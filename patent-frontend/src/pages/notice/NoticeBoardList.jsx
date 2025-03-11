// src/pages/notice/NoticeBoardList.jsx
import React, { useEffect, useState } from 'react';
import { useAuth } from '../../hooks/AuthContext';
import { useNavigate, Link } from 'react-router-dom';
import api from '../../services/api'; 
import './NoticeBoardList.css';

function NoticeBoardList() {
  const { isAuthenticated, user } = useAuth();
  const navigate = useNavigate();

  // 목록 데이터
  const [posts, setPosts] = useState([]);
  // 페이징
  const [page, setPage] = useState(0);
  const size = 10;
  const [totalPages, setTotalPages] = useState(1);

  // 마운트 시 목록 불러오기
  useEffect(() => {
    // 공지사항은 관리자/회원만 열람 가능 (백엔드 권한로직 + 프론트 보조)
    if (!isAuthenticated) {
      alert('로그인 후 열람 가능합니다.');
      navigate('/login');
      return;
    }

    loadPosts();
    // eslint-disable-next-line
  }, [page]);

  async function loadPosts() {
    try {
      const res = await api.get(`/api/board/NOTICE?page=${page}&size=${size}`);
      const data = res.data;
      setPosts(data.content || []);
      setTotalPages(data.totalPages || 1);
    } catch (err) {
      console.error('공지사항 목록 로딩 실패:', err);
      alert('공지사항 목록 로딩 실패: ' + err.message);
    }
  }

  const handleWrite = () => {
    // 관리자만 작성 (백엔드도 체크, 프론트에서 추가 방어)
    if (user?.role !== 'ROLE_ADMIN') {
      alert('관리자만 작성할 수 있습니다.');
      return;
    }
    navigate('/notice/write');
  };

  return (
    <div className="notice-board-list">
      <h2>공지사항 게시판</h2>
      <button onClick={handleWrite}>글쓰기</button>

      {posts.length > 0 ? (
        <table className="notice-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>제목</th>
              <th>조회수</th>
              <th>작성일</th>
            </tr>
          </thead>
          <tbody>
            {posts.map((post) => (
              <tr key={post.id}>
                <td>{post.id}</td>
                <td>
                  <Link to={`/notice/${post.id}`}>{post.title}</Link>
                </td>
                <td>{post.views}</td>
                <td>{post.createdAt}</td>
              </tr>
            ))}
          </tbody>
        </table>
      ) : (
        <p>등록된 공지사항이 없습니다.</p>
      )}

      {/* 페이징 */}
      <div className="notice-pagination">
        <button onClick={() => setPage((prev) => Math.max(prev - 1, 0))}
                disabled={page === 0}>
          이전
        </button>
        <span> {page + 1} / {totalPages} </span>
        <button onClick={() => setPage((prev) => (prev + 1 < totalPages ? prev + 1 : prev))}
                disabled={page + 1 >= totalPages}>
          다음
        </button>
      </div>
    </div>
  );
}

export default NoticeBoardList;
