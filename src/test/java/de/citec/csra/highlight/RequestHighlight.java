/*
 * Copyright (C) 2017 pholthau
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.citec.csra.highlight;

import com.google.protobuf.ByteString;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import rsb.Factory;
import rsb.Informer;
import rsb.InitializeException;
import rsb.RSBException;
import rsb.converter.ConverterRepository;
import rsb.converter.DefaultConverterRepository;
import rsb.converter.ProtocolBufferConverter;
import rsb.patterns.RemoteServer;
import rst.communicationpatterns.TaskStateType.TaskState;
import static rst.communicationpatterns.TaskStateType.TaskState.Origin.SUBMITTER;
import static rst.communicationpatterns.TaskStateType.TaskState.State.INITIATED;

/**
 *
 * @author pholthau
 */
public class RequestHighlight {

	public static void main(String[] args) throws InitializeException, RSBException, InterruptedException, ExecutionException, TimeoutException {
		ConverterRepository rep = DefaultConverterRepository.getDefaultConverterRepository();
		rep.addConverter(new ProtocolBufferConverter<>(TaskState.getDefaultInstance()));
		
		RemoteServer s = Factory.getInstance().createRemoteServer("/home/highlight/cfg");
		s.activate();
		s.call("setToken", "");
		s.deactivate();
		
		Informer i = Factory.getInstance().createInformer("/home/highlight/target");
		i.activate();
		TaskState t = TaskState.newBuilder().
				setOrigin(SUBMITTER).
				setState(INITIATED).
				setSerial(0).
				setWireSchema(ByteString.copyFromUtf8("utf-8-string")).
				setPayload(ByteString.copyFromUtf8("ENTRANCE,AMBIENT_LIGHT,6000000")).
				build();
		i.publish(t);
		i.deactivate();
	}

}
