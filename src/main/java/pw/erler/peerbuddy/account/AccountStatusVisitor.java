package pw.erler.peerbuddy.account;

import pw.erler.peerbuddy.account.p2p.P2PAccountStatus;

public interface AccountStatusVisitor<T> {

	public T visit(BasicAccountStatus status);

	public T visit(P2PAccountStatus status);

}
