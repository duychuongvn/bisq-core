/*
 * This file is part of Bisq.
 *
 * Bisq is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * Bisq is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Bisq. If not, see <http://www.gnu.org/licenses/>.
 */

package bisq.core.dao.blockchain.vo;

import bisq.core.dao.blockchain.vo.util.TxIdIndexTuple;

import bisq.common.proto.persistable.PersistablePayload;

import io.bisq.generated.protobuffer.PB;

import java.util.Optional;

import lombok.Data;

import javax.annotation.Nullable;

/**
 * An input is really just a reference to the spending output. It gets identified by the
 * txId and the index of the output. We use TxIdIndexTuple to encapsulate that.
 */
@Data
public class TxInput implements PersistablePayload {
    //TODO rename to make it more clear
    // TxId of the spending tx (tx from the connected tx output)
    private final String txId;
    // Index of the connected tx output
    private final int txOutputIndex;
    @Nullable
    private TxOutput connectedTxOutput;

    public TxInput(String txId, int txOutputIndex) {
        this(txId, txOutputIndex, null);
    }


    ///////////////////////////////////////////////////////////////////////////////////////////
    // PROTO BUFFER
    ///////////////////////////////////////////////////////////////////////////////////////////

    private TxInput(String txId, int txOutputIndex, @Nullable TxOutput connectedTxOutput) {
        this.txId = txId;
        this.txOutputIndex = txOutputIndex;
        this.connectedTxOutput = connectedTxOutput;
    }

    public PB.TxInput toProtoMessage() {
        final PB.TxInput.Builder builder = PB.TxInput.newBuilder()
                .setTxId(txId)
                .setTxOutputIndex(txOutputIndex);

        Optional.ofNullable(connectedTxOutput).ifPresent(e -> builder.setConnectedTxOutput(e.toProtoMessage()));

        return builder.build();
    }

    public static TxInput fromProto(PB.TxInput proto) {
        return new TxInput(proto.getTxId(),
                proto.getTxOutputIndex(),
                proto.hasConnectedTxOutput() ? TxOutput.fromProto(proto.getConnectedTxOutput()) : null);
    }


    ///////////////////////////////////////////////////////////////////////////////////////////
    // API
    ///////////////////////////////////////////////////////////////////////////////////////////

    public void reset() {
        connectedTxOutput = null;
    }

    public TxIdIndexTuple getTxIdIndexTuple() {
        return new TxIdIndexTuple(txId, txOutputIndex);
    }

    @Override
    public String toString() {
        return "TxInput{" +
                "\n     txId=" + txId +
                ",\n     txOutputIndex=" + txOutputIndex +
                ",\n     connectedTxOutput='" + connectedTxOutput + '\'' +
                "\n}";
    }
}
