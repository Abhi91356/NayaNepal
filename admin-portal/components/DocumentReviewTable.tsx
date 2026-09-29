import { useState } from 'react';
import { useQuery } from 'react-query';
import axios from 'axios';

interface Document {
  id: string;
  userId: string;
  userName: string;
  type: string;
  imageUrl: string;
  status: 'pending' | 'approved' | 'rejected';
  uploadedAt: string;
}

export function DocumentReviewTable() {
  const [page, setPage] = useState(0);
  const pageSize = 20;

  const { data, isLoading, error } = useQuery(
    ['documents', page],
    async () => {
      const res = await axios.get('/api/documents/pending', {
        params: { page, pageSize, status: 'pending' }
      });
      return res.data;
    }
  );

  const approveDocument = async (id: string) => {
    // Logic to approve document
    console.log('Approved', id);
  };

  const rejectDocument = async (id: string) => {
    // Logic to reject document
    console.log('Rejected', id);
  };

  if (isLoading) return <div>Loading...</div>;
  if (error) return <div>Error loading documents</div>;

  return (
    <div className="overflow-x-auto p-4">
      <table className="min-w-full bg-white border-collapse border border-gray-300 shadow-sm rounded-lg">
        <thead className="bg-gray-100">
          <tr>
            <th className="border p-3 text-left">User</th>
            <th className="border p-3 text-left">Document Type</th>
            <th className="border p-3 text-left">Uploaded At</th>
            <th className="border p-3 text-left">Preview</th>
            <th className="border p-3 text-center">Action</th>
          </tr>
        </thead>
        <tbody>
          {data?.documents.map((doc: Document) => (
            <tr key={doc.id} className="hover:bg-gray-50 transition-colors">
              <td className="border p-3">{doc.userName}</td>
              <td className="border p-3">{doc.type}</td>
              <td className="border p-3">{new Date(doc.uploadedAt).toLocaleDateString()}</td>
              <td className="border p-3">
                <a href={doc.imageUrl} target="_blank" rel="noopener noreferrer" className="text-indigo-600 font-medium hover:underline">
                  View Image
                </a>
              </td>
              <td className="border p-3 text-center">
                <button onClick={() => approveDocument(doc.id)} className="bg-green-600 hover:bg-green-700 text-white px-4 py-1.5 rounded-full text-sm mr-2 shadow-sm">
                  Approve
                </button>
                <button onClick={() => rejectDocument(doc.id)} className="bg-red-600 hover:bg-red-700 text-white px-4 py-1.5 rounded-full text-sm shadow-sm">
                  Reject
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      <div className="mt-6 flex items-center justify-between">
        <button
          onClick={() => setPage(Math.max(0, page - 1))}
          disabled={page === 0}
          className="px-4 py-2 bg-white border rounded shadow-sm disabled:opacity-50"
        >
          Previous
        </button>
        <span className="text-gray-600">
          Page {page + 1} of {Math.ceil((data?.total || 0) / pageSize)}
        </span>
        <button
          onClick={() => setPage(page + 1)}
          disabled={page >= Math.ceil((data?.total || 0) / pageSize) - 1}
          className="px-4 py-2 bg-white border rounded shadow-sm disabled:opacity-50"
        >
          Next
        </button>
      </div>
    </div>
  );
}
