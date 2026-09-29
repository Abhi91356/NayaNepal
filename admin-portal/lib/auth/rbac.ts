export enum Role {
  SUPER_ADMIN = 'super_admin',
  DOCUMENT_REVIEWER = 'document_reviewer',
  GOV_ADMIN = 'gov_admin',
  CONTENT_MODERATOR = 'content_moderator',
  PARTNER_ADMIN = 'partner_admin'
}

export const rolePermissions: Record<Role, string[]> = {
  [Role.SUPER_ADMIN]: [
    'users:read', 'users:create', 'users:update', 'users:delete',
    'documents:read', 'documents:approve', 'documents:reject',
    'analytics:read', 'settings:manage', 'admins:manage'
  ],
  [Role.DOCUMENT_REVIEWER]: [
    'documents:read', 'documents:approve', 'documents:reject'
  ],
  [Role.GOV_ADMIN]: [
    'analytics:read', 'subsidies:manage', 'reports:view'
  ],
  [Role.CONTENT_MODERATOR]: [
    'content:read', 'content:approve', 'content:reject'
  ],
  [Role.PARTNER_ADMIN]: [
    'users:read', 'transactions:read'
  ]
};

export function hasPermission(role: Role, permission: string): boolean {
  return rolePermissions[role]?.includes(permission) ?? false;
}
