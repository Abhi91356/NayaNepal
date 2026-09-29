import { GetServerSideProps } from 'next';
import { getServerSession } from 'next-auth';
import { authOptions } from '../../pages/api/auth/[...nextauth]';
import { Role } from './rbac';

export function withAuth(requiredRoles?: Role[]) {
  return (gssp: GetServerSideProps) => {
    return async (context: any) => {
      const session = await getServerSession(context.req, context.res, authOptions);

      if (!session) {
        return {
          redirect: {
            destination: '/login',
            permanent: false
          }
        };
      }

      const userRole = (session.user as any).role;

      if (requiredRoles && !requiredRoles.includes(userRole)) {
        return {
          redirect: {
            destination: '/unauthorized',
            permanent: false
          }
        };
      }

      return gssp(context);
    };
  };
}
