

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

public class MultiChannelBuildToolMain {
	/**
	 * 所需打渠道包的原始apk
	 */
	public static String sourceApkName = "source.apk";
	
	public static void main(String[] args) {
		long time = System.currentTimeMillis();
		File file = new File("info"+File.separator+"channel.txt");
		if(!file.exists()){
			return;
		}
		ArrayList<String> channels = getChannels(file);
		
		try {
			File channelFile = null;
			File sourceFile = new File(sourceApkName);
			
			//创建output目录 原目录下channel文件夹
			File output = new File("output");
			if (!output.exists()) {
				output.mkdirs();
			}
			
			for (int i=0 ;i<channels.size();i++) {
				String target_apkPath = "output"+File.separator+sourceApkName.substring(0, sourceApkName.lastIndexOf("."))+"_"+channels.get(i)+".apk";
				FileUtils.copyFile(sourceFile, new File(target_apkPath));
				//获取apk文件
				ZipFile zipFile = new ZipFile(target_apkPath);
				ZipParameters parameters = new ZipParameters();
				parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE); 
				parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
				parameters.setRootFolderInZip("META-INF/");
				 channelFile = new File("zhwnlchannel_"+channels.get(i));
				 if (!channelFile.exists()) {
					 channelFile.createNewFile();
				 }
				 
				 System.out.println("channelFile " + channelFile.getAbsolutePath());
				 zipFile.addFile(channelFile, parameters);
				 channelFile.delete();
			}
		} catch (Exception e) {
			System.out.println("error " + e.toString());
		} 
		System.out.println((System.currentTimeMillis()-time)+"毫秒");
	}
	
	private static ArrayList<String> getChannels(File file){
		ArrayList<String> channels = new ArrayList<String>();
		try{
			BufferedReader reader = new BufferedReader(
	                new InputStreamReader(new FileInputStream(file)));
	        String line = "";
	        while ((line = reader.readLine()) != null) {
	        	channels.add(line);
	        }
	        reader.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return channels;
	}
	

}
