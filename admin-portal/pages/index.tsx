import { useSession } from 'next-auth/react';
import { withAuth } from '../lib/auth/withAuth';
import { Role } from '../lib/auth/rbac';

function Dashboard() {
  const { data: session } = useSession();

  return (
    <div className="p-10 bg-white min-h-screen">
      <header className="mb-12">
        <h1 className="text-5xl font-black text-gray-900 leading-tight mb-4">
          NayaNepal <span className="text-indigo-600">Admin</span>
        </h1>
        <p className="text-xl text-gray-500 max-w-2xl">
          Welcome back, <span className="font-bold text-gray-900">{(session?.user as any)?.name}</span>.
          You are logged in as a <span className="underline decoration-indigo-500 decoration-2 italic">{(session?.user as any)?.role}</span>.
        </p>
      </header>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
        <div className="p-8 bg-indigo-600 rounded-3xl text-white shadow-xl hover:scale-[1.02] transition-transform cursor-pointer">
          <h3 className="text-sm font-bold uppercase tracking-widest opacity-80 mb-2">Pillar A</h3>
          <h2 className="text-3xl font-bold mb-6">Identity & Payments</h2>
          <p className="opacity-90">Verify documents, manage wallets, and track national transactions.</p>
        </div>

        <div className="p-8 bg-green-500 rounded-3xl text-white shadow-xl hover:scale-[1.02] transition-transform cursor-pointer">
          <h3 className="text-sm font-bold uppercase tracking-widest opacity-80 mb-2">Pillar B</h3>
          <h2 className="text-3xl font-bold mb-6">Marketplace</h2>
          <p className="opacity-90">Approve local listings, manage categories, and support MSMEs.</p>
        </div>

        <div className="p-8 bg-amber-500 rounded-3xl text-white shadow-xl hover:scale-[1.02] transition-transform cursor-pointer">
          <h3 className="text-sm font-bold uppercase tracking-widest opacity-80 mb-2">System</h3>
          <h2 className="text-3xl font-bold mb-6">Compliance</h2>
          <p className="opacity-90">View real-time audit logs and system performance metrics.</p>
        </div>
      </div>
    </div>
  );
}

export const getServerSideProps = withAuth([Role.SUPER_ADMIN, Role.GOV_ADMIN])(async () => {
  return { props: {} };
});

export default Dashboard;
